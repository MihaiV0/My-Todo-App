<script setup lang="ts">

const props = defineProps({
    text: String,
    showTooltip: Boolean,
    topPosition: String,
    leftPosition: String,
})

</script>

<template>
    <div 
        v-show="showTooltip"
        class="tooltip"
        :style="{ 
            top: topPosition,
            left: leftPosition
        }"
        v-html="text"
    >
    </div>
</template>

<style scoped>
    .tooltip {
        background-color: var(--main-color);
        color: var(--background-color);
        position: absolute;
        z-index: 1;
        padding: 5px;
        border-radius: 5px;
        font-size: 14px;
    }
</style>