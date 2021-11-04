<template>
  <div>
    <Todo-Detail-Contents v-for="(detail, index) in detailList" :key="index" :detail="detail" />
  </div>
</template>

<script>
import TodoDetailContents from '@/components/TodoDetailContents.vue';
export default {
  name: 'TODOCONTENTS',
  components: {
    TodoDetailContents,
  },
  data() {
    return {
      detailList: [
        {
          name: '조용일',
          content:
            '회원가입 디자인 견본입니다. https://www.naver.com/ <a href="www.naver.com">Hi</a>회원가입 디자인 견본입니다. https://www.naver.com/ <a href="www.naver.com">Hi</a>회원가입 디자인 견본입니다. https://www.naver.com/ <a href="www.naver.com">Hi</a>회원가입 디자인 견본입니다. https://www.naver.com/ <a href="www.naver.com">Hi</a>회원가입 디자인 견본입니다. https://www.naver.com/ <a href="www.naver.com">Hi</a> scroll_type2 scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2scroll_type2',
          regDate: '2021-10-21',
        },
        {
          name: '최광진',
          content: '# 코멘트입니다. [코멘트](https://www.naver.com/)',
          regDate: '2021-10-18',
        },
        {
          name: '최광진',
          content: '<div><h1 id="text">으악</h1></div>',
          regDate: '2021-10-18',
        },
        {
          name: '최광진',
          content: 'console.log(this.$router.Header)',
          regDate: '2021-10-18',
        },
      ],
    };
  },
};
</script>
