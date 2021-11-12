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
          상세내용 수정
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">
          <textarea
            class="
              overflow-y-auto
              scroll_type2
              bg-contentGray
              h-4/6
              rounded-md
              m-5
              mb-0
              p-3
              resize-none
              appearance-none
              focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
            "
            v-model="inputContent"
            @input="update"
            placeholder="내용을 입력해주세요."
          ></textarea>
          <div class="flex justify-end mx-5">
            <span :class="{ 'text-red-400': wordCnt > 1000, 'font-black': wordCnt > 1000 }">{{
              wordCnt
            }}</span>
            &nbsp;/ 1000 Byte
          </div>
          <div
            v-html="compiledMarkdown"
            class="
              todo-content
              h-2/6
              bg-contentGray
              mx-5
              mt-3
              p-3
              rounded-md
              overflow-y-auto
              scroll_type2
              break-all
            "
          ></div>
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
              mr-10
            "
            @click="closeModal()"
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
            @click="modifyTodoDetail()"
          >
            수정
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
import _ from 'lodash';
import { modifyTodoContent } from '@/api/todo.js';
import { mapGetters } from 'vuex';
export default {
  name: 'TODODETAILMODIFYMODAL',
  props: ['todoId', 'detailId', 'detailContent'],
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
  mounted() {
    // this.inputContent = this.detailContent;
    this.checkByte(this.detailContent);
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeModal');
    },
    checkByte(text_val) {
      // const maxByte = 1000; //최대 1000바이트
      const text_len = text_val.length; //입력한 문자수

      let totalByte = 0;
      for (let i = 0; i < text_len; i++) {
        const each_char = text_val.charAt(i);
        const uni_char = escape(each_char); //유니코드 형식으로 변환
        if (uni_char.length > 4) {
          // 한글 : 2Byte
          totalByte += 2;
        } else {
          // 영문,숫자,특수문자 : 1Byte
          totalByte += 1;
        }
      }

      this.wordCnt = totalByte;
      this.inputContent = text_val;
    },
    update: _.debounce(function (e) {
      this.checkByte(e.target.value);
    }, 300),
    modifyTodoDetail() {
      if (this.wordCnt > 1000) {
        alert('최대 1000byte 까지 입력이 가능합니다.');
        return;
      }

      modifyTodoContent(
        { id: this.detailId, contents: this.inputContent, memberId: this.id },
        () => {
          // console.log('수정완료!', this.inputContent, this.todoId, this.detailId);
          this.closeModal(true);
        },
        (error) => {
          console.error(error);
          alert('오류가 발생했습니다.');
          this.closeModal(false);
        }
      );
    },
  },
  computed: {
    ...mapGetters(['id']),
    compiledMarkdown: function () {
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

      let changeContent = this.inputContent
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
