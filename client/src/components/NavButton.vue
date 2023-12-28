<script setup lang="ts">
const props = defineProps({
    text: String,
});

const emits = defineEmits(['button-click']);

</script>

<template>
    <button @click="emits('button-click')">
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
    }

    button:hover {
        cursor: pointer;
        background-color: var(--grey-secondary-color);
        color: var(--main-color);
    }
</style>