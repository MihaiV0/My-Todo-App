<script setup lang="ts">
const props = defineProps({
    text: String
});
</script>

<template>
    <div id="title-container">
        <h1>
            {{ text }}
        </h1>
    </div>
</template>

<style scoped>
    #title-container {
        margin-bottom: 7vh;
    }

    h1 {
        font-size: 40px;
    }
</style>