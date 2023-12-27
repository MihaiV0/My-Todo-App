<script setup lang="ts">
import CustomButton from '@/components/CustomButton.vue';
import type router from '@/router';


</script>

<template>
    <div class="container">
        <span class="text">
            Register Successful!
        </span>
        <CustomButton
            text="Go to login page"
            @button-click="$router.push('/login')"
        />
    </div>
</template>

<style scoped>
    .container {
        position: relative;
        top: 40vh;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        gap: 3vh;
    }

    .text {
        font-size: 30px;
        font-weight: bold;
    }
</style>