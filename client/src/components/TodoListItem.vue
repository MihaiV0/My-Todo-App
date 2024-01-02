<script setup lang="ts">
const props = defineProps({
    title: String,
    activeTodo: Boolean,
});

const emits = defineEmits(['show-todo-description']);

function showTodoDesciption() {
    emits('show-todo-description');
}
</script>

<template>
    <div 
        class="todo-list-item"
        @click="showTodoDesciption"
        :style="{ backgroundColor: activeTodo ? 'var(--background-color)' : '' }"
    >
        {{ title }}
    </div>
</template>

<style scoped>
    .todo-list-item {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 1.3vh 0;
        color: var(--main-color);
        background-color: var(--grey-secondary-color);
    }

    .todo-list-item:hover {
        cursor: pointer;
        background-color: var(--background-color);
    }

    .todo-list-item:not(:nth-last-child(-n+2)) {
        border-bottom: 1px solid var(--main-color);
    }
</style>