<script setup lang="ts">
const mProps = defineProps({
    members: {
        required: true,
        type: Array<String>
    }
});

function replaceUnderscoresWithSpaces(nameWithUnderscores: String) {
    const nameParts = nameWithUnderscores.split('_');
    let nameWithSpaces = nameParts[0];
    for (let i = 1; i < nameParts.length; i++) {
        nameWithSpaces += ' ';
        nameWithSpaces += nameParts[i];
    }
    return nameWithSpaces;
}
</script>

<template>
    <div id="users-container">
        <div
            class="user"
            v-for="member in members"
        >
            <span class="material-symbols-outlined">
                face
            </span>
            {{ replaceUnderscoresWithSpaces(member) }}
        </div>
    </div>
</template>

<style scoped>
    #users-container {
        width: 20vw;
        display: flex;
        flex-direction: column;
        border-left: var(--todo-panel-border-right-width) solid var(--main-color);
        overflow: auto;
        background-color: var(--grey-secondary-color);
        position: absolute;
        right: 0;
        height: calc(100vh - var(--navbar-height) - var(--navbar-border-bottom-height) - var(--toolbar-height) 
            - var(--toolbar-border-bottom-height));
    }

    .user {
        display: flex;
        align-items: center;
        justify-content: left;
        padding: 0.4vh 0;
        color: var(--main-color);
    }
</style>