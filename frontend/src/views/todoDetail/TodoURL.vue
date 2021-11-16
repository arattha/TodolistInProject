<template>
  <div>
    <Todo-url-card v-for="(todoUrl, index) in todoUrlList" :key="index" :todoUrl="todoUrl" />
  </div>
</template>

<script>
import { getTodoUrls } from '@/api/todo.js';
import { formatDate } from '@/api/utils.js';
import { mapGetters } from 'vuex';
import TodoUrlCard from '@/components/TodoUrlCard.vue';
export default {
  name: 'TODOURL',
  components: {
    TodoUrlCard,
  },
  watch: {
    todoId: {
      handler(id) {
        if (id) {
          getTodoUrls(
            id,
            (res) => {
              for (let i = 0; i < res.object.length; i++) {
                let item = res.object[i];
                let temp = {
                  id: item['id'],
                  writer: item['writer'],
                  url: item['url'],
                  regDate: formatDate(item['regDate']),
                };
                this.todoUrlList.push(temp);
              }
            },
            (error) => {
              if (this.isLogin) {
                alert('프로젝트 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
                console.log(error);
              }
            }
          );
        }
      },
      immediate: true,
    },
  },
  computed: {
    ...mapGetters(['todoId']),
  },
  data() {
    return {
      todoUrlList: [],
    };
  },
};
</script>
