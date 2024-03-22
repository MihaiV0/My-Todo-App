<script setup lang="ts">
import AddNewGroupPopup from '@/components/AddNewGroupPopup.vue';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';
import GroupsPanel from '@/components/GroupsPanel.vue';
import ToolbarBase from '@/components/ToolbarBase.vue';
import ToolbarButtonBase from '@/components/ToolbarButtonBase.vue';
import UsersPanel from '@/components/UsersPanel.vue';
import { addGroup, loadAllGroups } from '@/data/server';
import { computed, onMounted, ref } from 'vue';

interface Group {
    id: number,
    groupName: string,
    members: string[],
};

const mGroups = ref<Array<Group>>([]);
const mActiveGroupName = ref('');
const mShowAddGroupPopup = ref(false);
const mShowErrorMessagePopup = ref(false);
const mActiveGroupIndex = computed(() => 
    mGroups.value.findIndex(group => group.groupName == mActiveGroupName.value)
);

function loadGroupsFromServer() {
    loadAllGroups()
        .then((groups: Array<Group>) => {
            mGroups.value = groups;
        });
}

onMounted(() => {
    loadGroupsFromServer();
});

function showCreateGroupPopup() {
    mShowAddGroupPopup.value = true;
}

function hideCreateGroupPopup() {
    mShowAddGroupPopup.value = false;
}

function changeActiveGroupId(groupName: string) {
    mActiveGroupName.value = groupName;
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

</script>

<template>
    <div id="groups-container">
        <ToolbarBase>
            <ToolbarButtonBase @click="showCreateGroupPopup">
                <span class="material-symbols-outlined green-sign">
                    group_add
                </span>
                Create new group
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
</style>