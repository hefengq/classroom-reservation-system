<template>
  <div class="login">
    <el-form class="login-panel" :model="form" label-position="top" @submit.prevent>
      <h1>教室预约系统</h1>
      <el-form-item label="账号">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-button type="primary" style="width:100%" :loading="loading" @click="submit">登录</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456' })

async function submit() {
  loading.value = true
  try {
    await auth.signIn(form)
    router.push(auth.role === 'ADMIN' ? '/admin' : '/board')
  } finally {
    loading.value = false
  }
}
</script>
