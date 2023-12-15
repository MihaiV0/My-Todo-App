<script setup lang="ts">
const emits = defineEmits(['close-popup']);

const props = defineProps({
    show: Boolean
});
</script>

<template>
    <Transition 
        name="popup"
    >
        <div 
            class="popup-mask"
            v-if="show"
            @click.self="emits('close-popup')"
        >
            <slot></slot>
        </div>
    </Transition>
</template>

<style scoped>
    .popup-mask {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: 999;
        display: flex;
        justify-content: center;
        align-items: center;
        transition: opacity 0.3s ease;
        background-color: rgba(0, 0, 0, 0.1);
    }
</style>