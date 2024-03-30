<script setup lang="ts">
import AddNewGroupPopup from '@/components/AddNewGroupPopup.vue';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';
import GroupsPanel from '@/components/GroupsPanel.vue';
import ToolbarBase from '@/components/ToolbarBase.vue';
import ToolbarButtonBase from '@/components/ToolbarButtonBase.vue';
import UsersPanel from '@/components/UsersPanel.vue';
import GroupChat from '@/components/GroupChat.vue';
import ToolbarSeparator from '@/components/ToolbarSeparator.vue';
import { addGroup, addMessage, addUserToGroup, loadAllGroups, loadMessages, removeUserFromGroup } from '@/data/server';
import { computed, onMounted, ref, watch } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';

interface Message {
    message: string,
    username: string,
    dateTime: string,
}

interface Group {
    id: number,
    groupName: string,
    members: string[],
    messages: Message[],
};

const mGroups = ref<Array<Group>>([]);
const mActiveGroupName = ref('');
const mShowAddGroupPopup = ref(false);
const mShowErrorMessagePopup = ref(false);
const mIsCurrentUserPartOfGroup = computed(() => {
    if (!mGroups.value[mActiveGroupIndex.value]) {
        return false
    }
    const find = mGroups.value[mActiveGroupIndex.value].members.find(member => member == localStorage.getItem('username'))
    if (find) {
        return true
    }
    return false
})
const mActiveGroupIndex = computed(() => 
    mGroups.value.findIndex(group => group.groupName == mActiveGroupName.value)
);
let mTimerIdLoadMessages: number

function loadGroupsFromServer() {
    loadAllGroups()
        .then((groups: Array<Group>) => {
            mGroups.value = groups;
        });
}

onMounted(() => {
    loadGroupsFromServer();
});

onBeforeRouteLeave(() => {
    clearInterval(mTimerIdLoadMessages)
})

function showCreateGroupPopup() {
    mShowAddGroupPopup.value = true;
}

function hideCreateGroupPopup() {
    mShowAddGroupPopup.value = false;
}

function changeActiveGroupId(groupName: string) {
    mActiveGroupName.value = groupName;
    updateMessages(groupName);
    setTimeout(scrollToBottomOfChat, 0.001)
}

function updateMessages(groupName: string) {
    loadMessages(groupName)
        .then((res: Message[]) => {
            mGroups.value[mActiveGroupIndex.value].messages = res;
        })
}

function saveGroup(groupName: string) {
    addGroup(groupName)
        .then((group: Group) => {
            hideCreateGroupPopup();
            mGroups.value.push(group);
        })
        .catch(err => {
            hideCreateGroupPopup();
            mShowErrorMessagePopup.value = true;
        });
}

function hideErrorMessagePopup() {
    mShowErrorMessagePopup.value = false;
}

function saveMessageAndReloadChatMessages(messageText: string, username: string) {
    addMessage(mActiveGroupName.value, username, messageText)
        .then((res: Message) => {
            updateMessages(mActiveGroupName.value)
        })
    setTimeout(scrollToBottomOfChat, 40)
}

function scrollToBottomOfChat() {
    const messageContainer = document.getElementById('message-container')
    messageContainer.scrollTop = messageContainer.scrollHeight
}

watch(
    mActiveGroupIndex, 
    () => {
        clearInterval(mTimerIdLoadMessages)
        mTimerIdLoadMessages = setInterval(() => {
            updateMessages(mActiveGroupName.value)
        }, 100)
    }
)

function joinGroup() {
    addUserToGroup(mActiveGroupName.value, localStorage.getItem('username') || '')
        .then(res => {
            loadGroupsFromServer()
        })
}

function leaveGroup() {
    removeUserFromGroup(mActiveGroupName.value, localStorage.getItem('username') || '')
        .then(res => {
            loadGroupsFromServer()
        })
}

</script>

<template>
    <div id="groups-container">
        <ToolbarBase>
            <ToolbarButtonBase @click="showCreateGroupPopup">
                <span class="material-symbols-outlined green-sign">
                    add
                </span>
                Create new group
            </ToolbarButtonBase>
            <ToolbarSeparator v-show="mActiveGroupIndex > -1"/>
            <ToolbarButtonBase 
                @click="joinGroup"
                v-show="!mIsCurrentUserPartOfGroup && mActiveGroupIndex > -1"
            >
                <span class="material-symbols-outlined green-sign">
                    person_add
                </span>
                Join group
            </ToolbarButtonBase>
            <ToolbarButtonBase 
                @click="leaveGroup"
                v-show="mIsCurrentUserPartOfGroup && mActiveGroupIndex > -1"
            >
                <span class="material-symbols-outlined red-sign">
                    person_remove
                </span>
                Leave group
            </ToolbarButtonBase>
        </ToolbarBase>
        <div id="main-container">
            <GroupsPanel 
                :groups="mGroups" 
                @group-changed="changeActiveGroupId" 
                :active-group-name="mActiveGroupName" 
            />
            <UsersPanel 
                :members="mActiveGroupIndex > -1 ? mGroups[mActiveGroupIndex].members : []" 
            />
            <GroupChat 
                v-show="mIsCurrentUserPartOfGroup"
                :messages="mActiveGroupIndex > -1 ? mGroups[mActiveGroupIndex].messages : []" 
                @send-message="saveMessageAndReloadChatMessages"
            />
        </div>
    </div>
    <Teleport to="body">
        <AddNewGroupPopup 
            :show-popup="mShowAddGroupPopup" 
            @close="hideCreateGroupPopup" 
            @save-group="saveGroup" 
        />
        <ErrorMessagePopup 
            error-text="Group already exists" 
            :show-popup="mShowErrorMessagePopup"
            @close="hideErrorMessagePopup" 
        />
    </Teleport>
</template>

<style scoped>
    #groups-container {
        height: calc(100vh - var(--navbar-height) - var(--navbar-border-bottom-height));
    }

    .green-sign {
        color: #007900;
    }

    #main-container {
        display: flex;
        height: calc(100vh - var(--navbar-height) - var(--navbar-border-bottom-height) - var(--toolbar-height) 
            - var(--toolbar-border-bottom-height));
    }

    .red-sign {
        color: #C30000;
    }
</style>