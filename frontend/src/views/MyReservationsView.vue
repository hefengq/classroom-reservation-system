<template>
  <section class="page">
    <div class="page-title"><h2>我的预约</h2></div>
    <el-table :data="rows" border>
      <el-table-column prop="reservationDate" label="日期" />
      <el-table-column prop="classroomId" label="教室ID" width="90" />
      <el-table-column label="节次" width="120">
        <template #default="{ row }">{{ row.slotStart }} - {{ row.slotEnd }}</template>
      </el-table-column>
      <el-table-column prop="purpose" label="用途" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="rejectReason" label="驳回原因" />
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { myReservations } from '../api'

const rows = ref([])
onMounted(async () => { rows.value = await myReservations() })
const statusText = (s) => ['待审批', '已通过', '已驳回'][s] || '未知'
const statusType = (s) => s === 1 ? 'success' : s === 2 ? 'danger' : 'warning'
</script>
