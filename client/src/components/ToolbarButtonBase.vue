<script setup lang="ts">

</script>

<template>
    <button class="toolbar-btn">
        <slot></slot>
    </button>
</template>

<style scoped>
    .toolbar-btn {
        border: none;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 1vw;
        background-color: #E0E0E0;
        gap: 0.2vw;
    }

    .toolbar-btn:hover {
        cursor: pointer;
        background-color: var(--grey-secondary-color);
    }
</style>