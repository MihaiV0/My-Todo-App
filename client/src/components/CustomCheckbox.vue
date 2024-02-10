<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';


const mProps = defineProps({
    name: String,
    id: String,
    text: String,
    initialValue: {
        type: Boolean,
        required: true
    },
});

const mValue = ref(false);

const mEmits = defineEmits(['value-changed']);

watch(
    mValue,
    (oldValue, newValue) => {
        mEmits('value-changed', mValue.value);
    }
);

watch(
    () => mProps.initialValue,
    (oldValue, newValue) => {
        mValue.value = mProps.initialValue;
    }
);

onMounted(() => {
    mValue.value = mProps.initialValue;
});
</script>

<template>
    <div>
        <input 
            type="checkbox" 
            :name="name" 
            :id="id"
            v-model="mValue"
        >
        <label :for="id">
            {{ text }}
        </label>
    </div>
</template>

<style scoped>
    input {
        accent-color: var(--main-color);
    }
</style>