import MyProfileView from '@/views/MyProfileView.vue'
import MyTodosView from '@/views/MyTodosView.vue'
import ProjectsView from '@/views/ProjectsView.vue'
import RegisterSuccessView from '@/views/RegisterSuccessView.vue'
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default',
      component: LoginView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/register-success',
      name: 'register-success',
      component: RegisterSuccessView
    },
    {
      path: '/my-todos',
      name: 'my-todos',
      component: MyTodosView
    },
    {
      path: '/projects',
      name: 'projects',
      component: ProjectsView
    },
    {
      path: '/my-profile',
      name: 'my-profile',
      component: MyProfileView
    },
  ]
})

export default router
