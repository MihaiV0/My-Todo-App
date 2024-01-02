<script setup lang="ts">

</script>

<template>
    <div class="row">
        <slot></slot>
    </div>
</template>

<style scoped>
    .row {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        gap: 1vw;
    }
</style>