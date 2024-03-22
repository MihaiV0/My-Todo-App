<script setup lang="ts">
import { onMounted } from 'vue';
import GroupListItem from './GroupListItem.vue';

const props = defineProps({
    groups: {
        type: Array as () => { id: number, groupName: string }[],
        default: () => [],
        required: true
    },
    activeGroupName: {
        type: String,
        required: true
    }
});

const emits = defineEmits(['group-changed']);

function changeSelectedGroup(groupName: string) {
    emits('group-changed', groupName);
}

</script>

<template>
    <div id="groups-list">
        <GroupListItem
            v-for="group in groups"
            :key="group.id"
            :id="`group-${group.groupName}`"
            :is-active="activeGroupName == group.groupName"
            @click="changeSelectedGroup(group.groupName)"
            :group-name="group.groupName"
        />
        <div 
            class="no-groups"
            v-show="groups.length == 0"
        >
            No groups to show
        </div>
    </div>
</template>

<style scoped>
    #groups-list {
        width: 20vw;
        display: flex;
        flex-direction: column;
        border-right: 1px solid var(--main-color);
        overflow: auto;
        background-color: var(--grey-secondary-color);
    }

    .no-groups {
        color: var(--main-color);
        display: flex;
        justify-content: center;
        align-items: center;
        height: 20vh;
    }

</style>