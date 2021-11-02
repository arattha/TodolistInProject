<template>
  <div>
    <div
      v-if="status === '진행'"
      class="flex justify-center items-center rounded-2xl bg-progress w-16 h-8 text-sm font-black"
    >
      {{ status }}
    </div>
    <div
      v-if="status === '접수'"
      class="flex justify-center items-center rounded-2xl bg-add w-16 h-8 text-sm font-black"
    >
      {{ status }}
    </div>
    <div
      v-if="status === 'New'"
      class="flex justify-center items-center rounded-2xl bg-green-500 w-16 h-8 text-sm font-black"
    >
      {{ status }}
    </div>
    <div
      v-if="status === '완료'"
      class="flex justify-center items-center rounded-2xl bg-done w-16 h-8 text-sm font-black"
    >
      {{ status }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'TODOSTATUS',
  props: ['status'],
};
</script>
