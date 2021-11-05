<template>
  <div class="flex flex-col w-11/12 h-24 bg-contentGray rounded-lg my-3 px-8 py-3 shadow-lg">
    <div class="flex justify-between w-full">
      <div class="flex">
        <div class="flex items-end font-black text-lg">
          {{ todoHistory.diff.message }}
        </div>
      </div>
      <button
        class="
          my-auto
          bg-itemGray
          text-black text-sm
          font-semibold
          h-9
          w-20
          py-2
          px-2
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        v-if="todoHistory.isContent"
      >
        비교하기
      </button>
    </div>
    <div class="flex ml-5 text-xs items-end pt-2">수정일: {{ todoHistory.modifyDate }}</div>
  </div>
</template>

<script>
export default {
  name: 'TODOHISTORYCARD',
  props: ['todoHistory'],
  data() {
    return {};
  },
};
</script>
