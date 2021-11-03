<template>
  <div>
    <div
      v-if="status === 'New' || isDetail"
      class="flex justify-center items-center rounded-2xl bg-green-500 w-16 h-8 text-sm font-black"
      :class="{ 'opacity-30': isDetail && status !== 'New', 'mr-3': isDetail }"
    >
      New
    </div>
    <div
      v-if="status === '접수' || isDetail"
      class="flex justify-center items-center rounded-2xl bg-add w-16 h-8 text-sm font-black"
      :class="{ 'opacity-30': isDetail && status !== '접수', 'mr-3': isDetail }"
    >
      접수
    </div>
    <div
      v-if="status === '진행' || isDetail"
      class="flex justify-center items-center rounded-2xl bg-progress w-16 h-8 text-sm font-black"
      :class="{
        'opacity-30': isDetail && status !== '진행',
        'mr-3': isDetail,
      }"
    >
      진행
    </div>
    <div
      v-if="status === '완료' || isDetail"
      class="flex justify-center items-center rounded-2xl bg-done w-16 h-8 text-sm font-black"
      :class="{
        'opacity-30': isDetail && status !== '완료',
        'mr-3': isDetail,
      }"
    >
      완료
    </div>
    <div
      v-if="status === '진행하지않음' || isDetail"
      class="
        flex
        justify-center
        items-center
        text-center
        rounded-2xl
        bg-buttonGray
        w-16
        h-8
        text-xs
        font-black
      "
      :class="{
        'opacity-30': isDetail && status !== '진행하지않음',
      }"
    >
      진행하지 <br />
      않음
    </div>
  </div>
</template>

<script>
export default {
  name: 'TODOSTATUS',
  props: ['status', 'isDetail'],
};
</script>
