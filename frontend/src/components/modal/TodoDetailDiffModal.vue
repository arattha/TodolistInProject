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
      z-50
      bg-buttonGray bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        w-5/6
        h-5/6
        lg:w-2/3
        xl:w-1/2
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
          상세내용 비교
        </div>
        <div
          class="
            flex flex-col
            md:flex-row
            h-full
            w-full
            bg-itemGray
            overflow-y-auto
            scroll_type2
            pt-5
            px-5
          "
        >
          <div class="flex flex-col w-full h-1/2 md:h-full md:mr-5">
            <div class="flex flex-col w-full">
              <div class="flex h-10 w-30 text-xl font-black">Before</div>
              <div class="flex h-0 w-32 border-b-2 border-black mb-2"></div>
            </div>
            <div
              v-html="compiledBeforeMarkdown"
              class="
                mt-1
                todo-content
                h-full
                w-full
                px-3
                py-2
                bg-contentGray
                rounded-md
                overflow-y-auto
                scroll_type2
                break-all
              "
            ></div>
          </div>
          <div class="flex flex-col w-full h-1/2 md:h-full">
            <div class="flex flex-col w-full">
              <div class="flex h-10 w-30 text-xl font-black">After</div>
              <div class="flex h-0 w-32 border-b-2 border-black mb-2"></div>
            </div>
            <div
              v-html="compiledAfterMarkdown"
              class="
                mt-1
                todo-content
                h-full
                w-full
                px-3
                py-2
                bg-contentGray
                rounded-md
                overflow-y-auto
                scroll_type2
                break-all
              "
            ></div>
          </div>
        </div>
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
            "
            @click="closeModal(true)"
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import vClickOutside from 'v-click-outside';

export default {
  name: 'TODODETAILDIFFMODAL',
  data() {
    return {
      inputContent: '',
      beforeContent: '',
      wordCnt: 0,
      beforeByte: 0,
      isValid: true,
    };
  },
  props: ['todoHistory'],
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    // console.log(this.todoHistory.diff);
  },
  methods: {
    onClickOutside() {
      this.closeModal(true);
    },
    closeModal(val) {
      this.$emit('closeModal', val);
    },
  },
  computed: {
    compiledBeforeMarkdown: function () {
      marked.setOptions({
        renderer: new marked.Renderer(),
        gfm: true,
        headerIds: false,
        tables: true,
        breaks: true,
        pedantic: false,
        smartLists: true,
        smartypants: false,
      });

      let changeContent = this.todoHistory.diff.before
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');

      let changedText = DOMPurify.sanitize(marked(changeContent), { KEEP_CONTENT: false });

      // changedText = changedText.replace(regex, `<${tag} class=""`);
      return changedText;
    },
    compiledAfterMarkdown: function () {
      marked.setOptions({
        renderer: new marked.Renderer(),
        gfm: true,
        headerIds: false,
        tables: true,
        breaks: true,
        pedantic: false,
        smartLists: true,
        smartypants: false,
      });

      let changeContent = this.todoHistory.diff.after
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');

      let changedText = DOMPurify.sanitize(marked(changeContent), { KEEP_CONTENT: false });

      // changedText = changedText.replace(regex, `<${tag} class=""`);
      return changedText;
    },
  },
};
</script>
