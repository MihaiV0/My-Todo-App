<script setup lang="ts">
const props = defineProps({
    text: String,
    highlighted: Boolean,
    showProfileIcon: Boolean,
    showLogoutIcon: Boolean,
    showTodoIcon: Boolean,
    showProjectsIcon: Boolean,
});

const emits = defineEmits(['button-click']);

</script>

<template>
    <button 
        @click="emits('button-click')"
        :class="{ 'selected': highlighted, 'logout-btn': text == 'Logout' }"
    >
        <span 
            class="material-symbols-outlined"
            v-show="showProfileIcon"
        >
            manage_accounts
        </span>
        <span 
            class="material-symbols-outlined"
            v-show="showLogoutIcon"
        >
            logout
        </span>
        <span 
            class="material-symbols-outlined"
            v-show="showTodoIcon"
        >
            task
        </span>
        <span 
            class="material-symbols-outlined"
            v-show="showProjectsIcon"
        >
            communities
        </span>
        {{ text }}
    </button>
</template>

<style scoped>
    button {
        padding: 0 1.2vw;
        background-color: var(--green-secondary-color);
        color: white;
        border: none;
        font-size: 3vh;
        height: var(--navbar-height);
        display: flex;
        align-items: center;
        gap: 0.3vw;
    }

    button:hover {
        cursor: pointer;
        background-color: var(--grey-secondary-color);
        color: var(--main-color);
    }

    .selected {
        background-color: var(--grey-secondary-color);
        color: var(--main-color);
    }

    .logout-btn:hover {
        background-color: #C30000;
        color: white;
    }
</style>