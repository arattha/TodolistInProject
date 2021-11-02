<template>
  <div class="flex flex-shrink-0 items-center bg-menuGray h-16">
    <div
      class="
        flex
        justify-center
        items-center
        text-lg
        font-black
        cursor-pointer
        h-full
        w-36
        mr-5
        ml-3
        hover:text-white
      "
      :class="{ 'text-white': type === 'total' }"
      @click="goTotalTodo()"
    >
      전체 할일
    </div>
    <div
      class="
        flex
        justify-center
        items-center
        text-lg
        font-black
        cursor-pointer
        h-full
        w-36
        mr-5
        hover:text-white
      "
      :class="{ 'text-white': type === 'my' }"
      @click="goMyTodo()"
    >
      내 할일
    </div>
    <div
      class="
        flex
        justify-center
        items-center
        text-lg
        font-black
        cursor-pointer
        h-full
        w-36
        hover:text-white
      "
      :class="{ 'text-white': type === 'progress' }"
      @click="goTodoProgress()"
    >
      진행 상황
    </div>
  </div>
</template>

<script>
export default {
  name: 'HEADERTODOMENU',
  props: ['type'],
  methods: {
    goTotalTodo() {
      this.$router.push('/todo');
    },
    goMyTodo() {
      this.$router.push('/mytodo');
    },
    goTodoProgress() {
      this.$router.push('/progress');
    },
  },
};
</script>
