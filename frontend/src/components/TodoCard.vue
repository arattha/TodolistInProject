<template>
  <div
    id="todoCard"
    class="
      mx-auto
      bg-contentGray
      grid
      px-4
      w-11/12
      h-32
      rounded-lg
      cursor-pointer
      shadow-md
      hover:bg-opacity-40
    "
    @click="clicktodo"
  >
    <div class="flex justify-between items-center">
      <div class="flex items-center justify-center">
        <div class="rounded-full w-10 h-10 bg-white mr-3"></div>
        <div class="text-base font-bold">{{ todoInfo.name }}</div>
      </div>
      <div class="flex justify-center items-center">
        <div id="bookmark" class="mr-5">
          <!-- <i class="far fa-star"></i> -->
          <i class="fas fa-star text-white"></i>
          <i class="fas fa-star text-yellow-400"></i>
        </div>
        <Todo-Status :status="todoInfo.status" :isDetail="false" />
      </div>
    </div>
    <div class="flex justify-between items-center">
      <div class="text-lg font-medium">
        {{ todoInfo.todoName }}
      </div>
      <button
        class="
          sendBtn
          flex
          w-16
          h-8
          justify-center
          items-center
          bg-buttonGray
          text-black text-xs
          font-semibold
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
          z-50
        "
      >
        보내기
      </button>
    </div>
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
export default {
  name: 'TODOCARD',
  components: {
    TodoStatus,
  },
  props: ['todoInfo'],
  methods: {
    todoSend() {
      console.log('보내기');
    },
    bookmark() {},
    clicktodo(event) {
      let target = event.target;
      if (target == event.currentTarget.querySelector('.sendBtn')) {
        this.todoSend();
        return;
      }
      if (target == event.currentTarget.querySelector('.bookmark')) {
        this.bookmark();
        return;
      }
      console.log('누름 : ' + this.todoInfo.todoName);
    },
  },
};
</script>
