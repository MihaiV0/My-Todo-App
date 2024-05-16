<script setup lang="ts">
const mProps = defineProps({
    listTitle: String,
    hasRightBorder: Boolean,
    todos: {
        type: Array as () => { todoId: number, title: string }[],
        default: () => []
    },
})

const mEmits = defineEmits(['showTodoDescription'])
</script>

<template>
    <div 
        class="list-container"
        :class="{ 'right-border' : hasRightBorder }"
    >
        <h2>
            {{ listTitle }}
        </h2>
        <div class="list-items">
            <div 
                class="list-item"
                v-for="todo in todos"
                @click="$emit('showTodoDescription', todo.todoId)"
            >
                {{ todo.title }}
            </div>
        </div>
    </div>
</template>

<style scoped>
    .list-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: calc((100vw - 4px) / 3);
        background-color: var(--grey-secondary-color);
    }

    .right-border {
        border-right: 2px solid var(--main-color);
    }

    h2 {
        margin: 0;
        width: calc((100vw - 4px) / 3 - 3vw);
        padding-left: 3vw;
        padding-bottom: 1vh;
        padding-top: 1vh;
        border-bottom: 3px solid var(--main-color);
        background-color: var(--green-secondary-color);
        color: var(--main-color);
    }

    .list-items {
        display: flex;
        flex-direction: column;
        overflow: auto;
    }

    .list-item {
        width: calc((100vw - 4px) / 3 - 1vw);
        padding-left: 1vw;
        padding-top: 1vh;
        padding-bottom: 1vh;
        color: var(--main-color);
    }

    .list-item:hover {
        background-color: white;
        cursor: pointer;
    }

    .list-item:not(:last-child) {
        border-bottom: 1px solid var(--main-color);
    }
</style>