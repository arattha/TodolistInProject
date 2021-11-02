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
      @click="goTotalPjt()"
    >
      진행 프로젝트
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
      :class="{ 'text-white': type === 'done' }"
      @click="goDonePjt()"
    >
      완료 프로젝트
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
      @click="addPjt()"
    >
      프로젝트 추가
    </div>
  </div>
</template>

<script>
export default {
  name: 'HEADERPJTMENU',
  props: ['type'],
  methods: {
    goTotalPjt() {
      this.$router.push('/pjt');
    },
    goDonePjt() {
      this.$router.push('/donepjt');
    },
    addPjt() {
      console.log('프로젝트 추가');
    },
  },
};
</script>
