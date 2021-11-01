<template>
  <div class="flex flex-shrink-0 h-full w-96">
    <div class="bg-itemGray rounded-lg shadow-lg w-full">
      <div class="flex justify-between items-center px-5 py-2 my-3">
        <div class="flex border-b-2 w-24 pb-2 border-black text-xl font-black">
          {{ teamInfo.teamName }}
        </div>
        <button
          class="
            flex
            justify-center
            items-center
            bg-contentGray
            text-black text-sm
            font-semibold
            h-10
            w-24
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-menuGray
            focus:outline-none
            focus:ring-2
            focus:ring-headerGray
            focus:ring-offset-2
            focus:ring-offset-purple-200
          "
          @click="todoAdd()"
        >
          할일 추가
        </button>
      </div>
      <div
        class="
          flex flex-col
          focus-within:items-center
          justify-start
          text-xs
          w-full
          font-semibold
          px-5
          my-3
        "
      >
        <div class="flex flex-row w-full mb-2">
          <div class="w-1/2">전체 할일 : {{ teamInfo.totalCnt }}</div>
          <div class="w-1/2">접수한 일 : {{ teamInfo.addCnt }}</div>
        </div>
        <div class="flex flex-row w-full">
          <div class="w-1/2">완료한 일 : {{ teamInfo.doneCnt }}</div>
          <div class="w-1/2">진행중인 일 : {{ teamInfo.progressCnt }}</div>
        </div>
      </div>
      <div class="overflow-y-auto h-96 scroll_type2">
        <div class="mb-6" v-for="(todoInfo, index) in teamInfo.todoInfoList" :key="index">
          <Todo-Card :todoInfo="todoInfo" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TodoCard from '@/components/TodoCard.vue';
export default {
  name: 'TOTALKANBAN',
  components: {
    TodoCard,
  },
  props: ['teamInfo'],
  methods: {
    todoAdd() {
      console.log('할일 추가');
    },
  },
};
</script>
