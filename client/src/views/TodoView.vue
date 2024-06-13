<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { getTodoById, rateTodo } from '../data/server'
import { useRoute } from 'vue-router';
import CustomInput from '@/components/CustomInput.vue';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';

interface Rating {
    value: number
    userId: number
}

interface Todo {
    todoId: number;
    title: string;
    description: string;
    dueDate: string;
    status: string;
    priority: string;
    ratings: Rating[]
}

const mTodo = ref<Todo>()
const mRating = ref('')
const mErrorMessage = ref('')
const mShowErrorPopup = ref(false)

onMounted(() => {
    getTodoById(parseInt(useRoute().params.id))
        .then((res: Todo) => {
            mTodo.value = res
        })
})

function changeRating(newRating: string) {
    mRating.value = newRating
}

function postRating() {
    let numberRating = parseFloat(mRating.value)
    if (numberRating < 1 || numberRating > 5) {
        mErrorMessage.value = 'Rating must be between 1 and 5'
        mShowErrorPopup.value = true
    } else {
        rateTodo(localStorage.getItem('username'), mTodo.value?.todoId, mRating.value)
            .then(() => {
                mErrorMessage.value = 'Rating saved'
                mShowErrorPopup.value = true
            })
            .catch(err => {
                if (err.message == 'Unexpected token \'Y\', "You can\'t "... is not valid JSON') {
                    mErrorMessage.value = 'You can\'t rate your own todo!'
                } else if (err.message == 'Unexpected token \'Y\', "You alread"... is not valid JSON') {
                    mErrorMessage.value = 'You already rated this todo!'
                } else {
                    mErrorMessage.value = err.message
                }
                mShowErrorPopup.value = true
            })
    }
}

function closeErrorPopup() {
    mShowErrorPopup.value = false
}
</script>

<template>
    <div id="container">
        <h2>
            {{ 'Title: ' + mTodo?.title }}
        </h2>
        <div>
            {{ 'Description: ' + mTodo?.description }}
        </div>
        <div>
            {{ 'Due date: ' + mTodo?.dueDate }}
        </div>
        <div>
            {{ 'Priority: ' + mTodo?.priority }}
        </div>
        <div>
            {{ 'Status: ' + mTodo?.status }}
        </div>
        <div 
            v-show="mTodo?.status == 'CLOSED'"
            class="rating"
        >
            <CustomInput 
                input-type="text"
                placeholder-text="Your rating"
                :is-password-field="false"
                @change-value="changeRating"
                @enter-pressed="postRating"
            />
            <span class="material-symbols-outlined">
                star_rate
            </span>
        </div>
    </div>
    <Teleport to="body">
        <ErrorMessagePopup
            :error-text="mErrorMessage"
            :show-popup="mShowErrorPopup"
            @close="closeErrorPopup"
        />
    </Teleport>
</template>

<style scoped>
    #container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 1vh;
    }

    .rating {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>