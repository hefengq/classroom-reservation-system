import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import BoardView from '../views/BoardView.vue'
import AdminView from '../views/AdminView.vue'
import MyReservationsView from '../views/MyReservationsView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/board' },
    { path: '/login', component: LoginView },
    { path: '/board', component: BoardView },
    { path: '/admin', component: AdminView },
    { path: '/my', component: MyReservationsView }
  ]
})

router.beforeEach((to) => {
  if (to.path !== '/login' && !localStorage.getItem('token')) {
    return '/login'
  }
})

export default router
