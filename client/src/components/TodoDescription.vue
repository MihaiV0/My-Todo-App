<script setup lang="ts">
import { ref, watch } from 'vue';
import CustomInput from './CustomInput.vue';
import TodoField from './TodoField.vue';
import CustomSizeTextarea from './CustomSizeTextarea.vue';
import VueFlatpickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.min.css';
import DropDown from '@/components/DropDown.vue';
import FixedSizeSpan from '@/components/FixedSizeSpan.vue';

const props = defineProps({
    title: String,
    description: String,
    editing: Boolean,
    activeTodoId: Number,
    dueDate: String,
    status: String,
    priority: String,
});

const emits = defineEmits(['title-changed', 'description-changed', "date-changed", "status-changed", "priority-changed", 'copy-successful']);

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
const statusOptions = ["OPEN", "IN PROGRESS", "CLOSED"];
const priorityOptions = ["PRIO 1", "PRIO 2"];

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

function statusChanged(newStatus: string) {
    emits('status-changed', newStatus);
}

function priorityChanged(newPriority: string) {
    emits('priority-changed', newPriority);
}

function copyToClipboard(text: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
        const textarea = document.createElement('textarea');
        textarea.value = text;
        textarea.style.position = 'fixed';
        document.body.appendChild(textarea);
        textarea.focus();
        textarea.select();

        try {
            const successful = document.execCommand('copy');
            if (!successful) {
                reject(new Error('Unable to copy to clipboard'));
            } else {
                emits('copy-successful');
                resolve();
            }
        } catch (err) {
            reject(err);
        } finally {
            document.body.removeChild(textarea);
        }
    });
}

</script>

<template>
    <div 
        id="todo-description"
        v-show="title || (activeTodoId != -2)"
    >
        <TodoField>
            <h2 
                @click="copyToClipboard(`http://localhost:5173/todo/${activeTodoId}`)"
            >
                {{ editing ? initialTitle : title }}
            </h2>
        </TodoField>
        <TodoField>
            <FixedSizeSpan>
                Status:
            </FixedSizeSpan>
            <DropDown 
                :values="statusOptions"
                :initial-value="status"
                :drop-down-id="`status-dropdown-${activeTodoId}`"
                @value-changed="statusChanged"
                :enabled="editing"
                :custom-size="true"
            />
        </TodoField>
        <TodoField>
            <FixedSizeSpan>
                Priority:
            </FixedSizeSpan>
            <DropDown 
                :values="priorityOptions"
                :initial-value="priority"
                :drop-down-id="`priority-dropdown-${activeTodoId}`"
                @value-changed="priorityChanged"
                :enabled="editing"
                :custom-size="true"
            />
        </TodoField>
        <TodoField>
            <FixedSizeSpan>
                Due date:
            </FixedSizeSpan>
            <VueFlatpickr
                :config="flatpickrConfig"
                v-model:model-value="selectedDate"
                class="date-picker"
                @on-change="dateChanged(selectedDate)"
                :disabled="!editing"
            />
        </TodoField>
        <TodoField>
            <FixedSizeSpan>
                Title:
            </FixedSizeSpan>
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
            <FixedSizeSpan>
                Description:
            </FixedSizeSpan>
            <CustomSizeTextarea 
                :col-nr="60"
                :row-nr="5"
                :initial-value="description"
                @text-changed="descriptionChanged"
                placeholder-text="Todo description"
                :input-disabled="!editing"
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

    h2:hover {
        cursor: pointer;
        text-decoration: underline;
    }

</style>