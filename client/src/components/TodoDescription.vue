<script setup lang="ts">
import { ref, watch } from 'vue';
import CustomInput from './CustomInput.vue';
import TodoField from './TodoField.vue';
import CustomSizeTextarea from './CustomSizeTextarea.vue';
import VueFlatpickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.min.css';

const props = defineProps({
    title: String,
    description: String,
    editing: Boolean,
    activeTodoId: Number,
    dueDate: String,
});

const emits = defineEmits(['title-changed', 'description-changed', "date-changed"]);

function titleChanged(newTitle: string) {
    titleText.value = newTitle;
    emits('title-changed', newTitle);
}

function descriptionChanged(newDescription: string) {
    descriptionText.value = newDescription;
    emits('description-changed', newDescription);
}

const titleText = ref('');
const descriptionText = ref('');
const initialTitle = ref('');
const selectedDate = ref('');
const flatpickrConfig = {
    dateFormat: 'd.m.Y',
    minDate: "today"
}

watch(
    () => props.title, 
    (oldValue, newValue) => {
        titleText.value = props.title || '';
        descriptionText.value = props.description || '';
        selectedDate.value = props.dueDate || '';
    }
);

watch(
    () => props.editing,
    (oldValue, newValue) => {
        initialTitle.value = props.title || '';
    }
);

watch(
    () => props.dueDate,
    (oldValue, newValue) => {
        selectedDate.value = props.dueDate || '';
    }
);

function dateChanged(newDate: string) {
    emits('date-changed', newDate);
}

</script>

<template>
    <div 
        id="todo-description"
        v-show="title || (activeTodoId != -2)"
    >
        <TodoField>
            <h2>{{ editing ? initialTitle : title }}</h2>
        </TodoField>
        <TodoField>
            Title:
            <CustomInput
                input-type="text"
                placeholder-text="Title"
                :is-password-field="false"
                @change-value="titleChanged"
                :initial-value="title != 'New Todo' ? title : ''"
                :input-disabled="!editing"
                custom-width="35vw"
            />
        </TodoField>
        <TodoField>
            Description:
            <CustomSizeTextarea 
                :col-nr="60"
                :row-nr="5"
                :initial-value="description"
                @text-changed="descriptionChanged"
                placeholder-text="Todo description"
                :input-disabled="!editing"
            />
        </TodoField>
        <TodoField>
            Due date:
            <VueFlatpickr
                :config="flatpickrConfig"
                v-model:model-value="selectedDate"
                class="date-picker"
                @on-change="dateChanged(selectedDate)"
                :disabled="!editing"
            />
        </TodoField>
    </div>
</template>

<style scoped>
    #todo-description {
        width: calc(100% - var(--todo-panel-width) - var(--todo-panel-border-right-width));
        overflow: auto;
        padding: 1vh 1vw;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 2vh;
    }

    .date-picker {
        outline: none;
        font-size: var(--default-font-size);
        border: 0.5px solid transparent;
        padding: 5px 7px;
        font-family: Arial, Helvetica, sans-serif;
    }

    .date-picker:disabled {
        background-color: white;
        opacity: 0.7;
    }

    .date-picker:hover {
        border: 0.5px solid var(--main-color);
    }

    .date-picker:focus {
        border: 0.5px solid var(--main-color);
    }

    .date-picker:disabled {
        cursor: default;
    }

</style>