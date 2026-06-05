<template>
  <el-container class="app-shell">
    <el-aside v-if="auth.token" width="232px" class="sidebar">
      <div class="brand">教室预约系统</div>
      <el-menu :default-active="$route.path" router>
        <el-menu-item index="/board">预约看板</el-menu-item>
        <el-menu-item index="/my">我的预约</el-menu-item>
        <el-menu-item v-if="auth.role === 'ADMIN'" index="/admin">管理后台</el-menu-item>
      </el-menu>
      <div class="profile">
        <strong>{{ auth.nickname }}</strong>
        <span>{{ auth.role }}</span>
        <el-button size="small" @click="logout">退出</el-button>
      </div>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { onMounted, onUnmounted, watch } from 'vue'
import { ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const auth = useAuthStore()
const router = useRouter()
let socket

onMounted(connectSocket)
watch(() => auth.token, connectSocket)
onUnmounted(() => socket?.close())

function connectSocket() {
  socket?.close()
  if (auth.token && auth.userId) {
    const protocol = location.protocol === 'https:' ? 'wss' : 'ws'
    socket = new WebSocket(`${protocol}://${location.host}/ws/notify?token=${encodeURIComponent(auth.token)}`)
    socket.onmessage = (event) => {
      const body = JSON.parse(event.data)
      ElNotification({ title: '审批通知', message: body.message, type: 'success' })
    }
  }
}

function logout() {
  socket?.close()
  auth.logout()
  router.push('/login')
}
</script>
