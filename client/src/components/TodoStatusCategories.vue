<script setup lang="ts">
import TodoList from '@/components/TodoList.vue'

const mProps = defineProps({
    todos: {
        type: Array as () => { todoId: number, title: string, status: string }[],
        default: () => []
    },
})

const mEmits = defineEmits(['showDescriptionFromStatusList'])

function emitTodoId(todoId: number) {
    mEmits('showDescriptionFromStatusList', todoId)
}
</script>

<template>
    <div id="todo-lists">
        <TodoList
            list-title="Open"
            :has-right-border="true"
            :todos="todos.filter(todo => todo.status == 'OPEN')"
            @show-todo-description="emitTodoId"
        />
        <TodoList
            list-title="In progress"
            :has-right-border="true"
            :todos="todos.filter(todo => todo.status == 'IN PROGRESS')"
            @show-todo-description="emitTodoId"
        />
        <TodoList
            list-title="Closed"
            :has-right-border="false"
            :todos="todos.filter(todo => todo.status == 'CLOSED')"
            @show-todo-description="emitTodoId"
        />
    </div>
</template>

<style scoped>
    #todo-lists {
        display: flex;
    }
</style>