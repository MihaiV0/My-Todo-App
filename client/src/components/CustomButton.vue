<script setup lang="ts">
const props = defineProps({
    text: String,
});

const emits = defineEmits(['button-click']);

</script>

<template>
    <div class="button-container">
        <button
            @click="emits('button-click')"
        >
            {{ text }}
        </button>
    </div>
</template>

<style scoped>
    button {
        padding: 7px 14px;
        background-color: var(--main-color);
        color: white;
        border-color: var(--main-color);
        font-size: var(--default-font-size);
    }

    button:hover {
        border-color: currentColor;
        cursor: pointer;
    }
</style>