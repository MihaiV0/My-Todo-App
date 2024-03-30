<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { getTodoById } from '../data/server'
import { useRoute } from 'vue-router';

interface Todo {
    todoId: number;
    title: string;
    description: string;
    dueDate: string;
    status: string;
    priority: string;
}

const mTodo = ref<Todo>()

onMounted(() => {
    getTodoById(parseInt(useRoute().params.id))
        .then((res: Todo) => {
            mTodo.value = res
        })
})
</script>

<template>
    <div id="container">
        <h2>
            {{ 'Title: ' + mTodo?.title }}
        </h2>
        <div>
            {{ 'Description: ' + mTodo?.description }}
        </div>
        <div>
            {{ 'Due date: ' + mTodo?.dueDate }}
        </div>
        <div>
            {{ 'Priority: ' + mTodo?.priority }}
        </div>
        <div>
            {{ 'Status: ' + mTodo?.status }}
        </div>
    </div>
</template>

<style scoped>
    #container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 1vh;
    }
</style>