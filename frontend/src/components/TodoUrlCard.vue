<template>
  <div class="flex flex-col w-11/12 h-52 bg-contentGray rounded-lg my-3 px-8 py-3 shadow-lg">
    <div class="flex justify-between w-full">
      <div class="flex">
        <div class="flex items-end font-black text-2xl">{{ todoUrl.writer }}</div>
        <div class="flex ml-5 text-xs items-end pb-1">{{ todoUrl.regDate }}</div>
      </div>
    </div>
    <div
      v-html="compiledMarkdown"
      class="todo-content h-52 overflow-y-auto mt-5 scroll_type2 break-all"
    ></div>
  </div>
</template>

<script>
import { marked } from 'marked';
import DOMPurify from 'dompurify';

export default {
  name: 'TODOURLCARD',
  props: ['todoUrl'],
  data() {
    return {};
  },
  computed: {
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

      let changeContent = this.todoUrl.url
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
