<script setup lang="ts">

</script>

<template>
    <div id="loading">
        <div class="rotating-circle"></div>
    </div>
</template>

<style scoped>
    .rotating-circle {
        width: 100px;
        height: 100px;
        border: 10px solid var(--green-secondary-color);
        border-top: 10px solid transparent;
        border-radius: 50%;
        animation: spin 2s linear infinite;
    }

    @keyframes spin {
        0% {
            transform: rotate(0deg);
        }

        100% {
            transform: rotate(360deg);
        }
    }

    #loading {
        position: absolute;
        top: 0;
        z-index: 10;
        height: 100vh;
        width: 100vw;
        background-color: rgba(0, 0, 0, 0.3);
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>