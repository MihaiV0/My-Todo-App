<script setup lang="ts">
import CustomButton from './CustomButton.vue';
import PopupBase from './PopupBase.vue';
import { termsAndConditions } from '@/data/termsAndConditions';

const props = defineProps({
    showPopup: Boolean,
});

const emits = defineEmits(['close', 'acceptTerms']);

</script>

<template>
    <PopupBase 
        :show="showPopup" 
        @close-popup="emits('close')"
    >
        <div class="popup-body">
            <h2>
                Terms And Conditions
            </h2>
            <div 
                class="text-container"
                v-html="termsAndConditions"
            >
            </div>
            <CustomButton 
                text="Accept terms and conditions" 
                @button-click="emits('acceptTerms')" 
            />
        </div>
    </PopupBase>
</template>

<style scoped>
.popup-body {
    background-color: white;
    width: 45vw;
    height: 50vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 10px;
    border-radius: 10px;
}

.text-container {
    overflow: auto;
    border: 1px solid var(--main-color);
    height: 30vh;
    width: 40vw;
    margin-bottom: 2vh;
    padding: 4px;
}
</style>