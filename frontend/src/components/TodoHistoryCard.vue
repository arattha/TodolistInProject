<template>
  <div class="flex flex-col w-11/12 h-32 bg-contentGray rounded-lg my-3 shadow-lg py-3 px-5">
    <div class="flex justify-between w-full h-full">
      <div class="flex">
        <div class="flex items-center font-black text-lg">
          {{ todoHistory.diff.message }}
        </div>
      </div>
    </div>
    <div
      class="flex"
      :class="{ 'justify-between': todoHistory.isContent, 'justify-end': !todoHistory.isContent }"
    >
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
      <div class="flex justify-end text-xs items-center">{{ todoHistory.modifyDate }}</div>
    </div>
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
