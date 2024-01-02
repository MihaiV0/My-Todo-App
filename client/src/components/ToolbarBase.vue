<script setup lang="ts">

</script>

<template>
    <div id="toolbar">
        <slot></slot>
    </div>
</template>

<style scoped>
    #toolbar {
        display: flex;
        border-bottom: var(--toolbar-border-bottom-height) solid var(--main-color);
        height: var(--toolbar-height);
        background-color: white;
    }
</style>