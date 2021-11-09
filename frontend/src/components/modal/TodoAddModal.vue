<template>
  <div
    class="
      flex
      justify-center
      items-center
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-screen
      h-screen
      z-100
      bg-buttonGray bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        w-5/6
        sm:w-2/3
        h-5/6
        lg:w-5/12 lg:h-5/6
        mx-auto
        rounded-md
        shadow-lg
        fixed
        translate-x-1/2 translate-y-1/2
        bg-white
        z-50
      "
      v-click-outside="onClickOutside"
    >
      <div class="flex flex-col h-full w-full">
        <div
          class="
            flex
            rounded-t-md
            bg-headerGray
            h-1/6
            w-full
            justify-center
            items-center
            text-white
            font-black
            text-3xl
          "
        >
          상세내용 추가
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">test</div>
        <div class="flex justify-center items-center p-5 bg-itemGray">
          <button
            class="
              bg-red-600
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-red-800
              focus:outline-none
              focus:ring-2
              focus:ring-red-500
              focus:ring-offset-2
              focus:ring-offset-red-200
              mr-10
            "
            @click="closeModal"
          >
            닫기
          </button>
          <button
            class="
              bg-headerGray
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-blue-500
              focus:ring-offset-2
              focus:ring-offset-blue-200
            "
          >
            추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';

export default {
  name: 'TODOADDMODAL',
  data() {
    return {
      inputContent: '',
      beforeContent: '',
      wordCnt: 0,
      beforeByte: 0,
      isValid: true,
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeAddModal');
    },
  },
};
</script>
