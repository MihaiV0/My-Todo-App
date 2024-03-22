<script setup lang="ts">
const props = defineProps({
    isActive: Boolean,
    groupName: String,
})

const emits = defineEmits(['selected']);
</script>

<template>
    <div
        class="group"
        :style="{ backgroundColor: isActive ? 'var(--background-color)' : '' }"
        @click="emits('selected')"
    >
        {{ groupName }}
    </div>
</template>

<style scoped>
    .group {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 1.3vh 0;
        color: var(--main-color);
        background-color: var(--grey-secondary-color);
    }

    .group:hover {
        cursor: pointer;
        background-color: var(--background-color);
    }

    .group:not(:nth-last-child(-n+2)) {
        border-bottom: 1px solid var(--main-color);
    }
</style>