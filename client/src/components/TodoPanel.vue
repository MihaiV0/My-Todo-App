<script setup lang="ts">
import TodoListItem from './TodoListItem.vue';

const props = defineProps({
    todos: {
        type: Array as () => { todoId: number, title: string }[],
        default: () => []
    },
    activeTodoId: Number,
});

const emits = defineEmits(['show-todo-description']);

function showTodoDescription(todoId: number) {
    emits('show-todo-description', todoId);
}
</script>

<template>
    <div id="todo-container">
        <TodoListItem 
            v-for="todo in todos"
            :title="todo.title"
            @show-todo-description="showTodoDescription(todo.todoId)"
            :active-todo="activeTodoId == todo.todoId"
        />
        <div 
            class="no-todos"
            v-show="todos.length == 0"
        >
            No todos to show
        </div>
    </div>
</template>

<style scoped>
    #todo-container {
        display: flex;
        flex-direction: column;
        width: var(--todo-panel-width);
        background-color: var(--grey-secondary-color);
        border-right: var(--todo-panel-border-right-width) solid var(--main-color);
        overflow: auto;
    }

    .no-todos {
        color: var(--main-color);
        display: flex;
        justify-content: center;
        align-items: center;
        height: 20vh;
    }
</style>