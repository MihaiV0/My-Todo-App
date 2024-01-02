<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
    colNr: Number,
    rowNr: Number,
    placeholderText: String,
    initialValue: String,
    inputDisabled: Boolean,
});

const emits = defineEmits(['text-changed']);

const text = ref('');

watch(
    () => props.initialValue,
    (oldValue, newValue) => {
        text.value = props.initialValue || '';
    }
);
</script>

<template>
    <textarea 
        name="description"
        :cols="colNr" 
        :rows="rowNr"
        v-model="text"
        :placeholder="placeholderText"
        :disabled="inputDisabled"
        @keyup="emits('text-changed', text)"
    >
    </textarea>
</template>

<style scoped>
    textarea {
        outline: none;
        font-size: var(--default-font-size);
        border: 0.5px solid transparent;
        padding: 5px 7px;
        font-family: Arial, Helvetica, sans-serif;
    }

    textarea:disabled {
        background-color: white;
        opacity: 0.7;
    }

    textarea:hover {
        border: 0.5px solid var(--main-color);
    }

    textarea:focus {
        border: 0.5px solid var(--main-color);
    }
</style>