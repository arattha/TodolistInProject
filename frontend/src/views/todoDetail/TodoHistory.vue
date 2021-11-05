<template>
  <div>test</div>
</template>

<script>
import { getTodoRecords } from '@/api/todo.js';
import { formatDate } from '@/api/utils.js';
import { mapGetters } from 'vuex';
export default {
  name: 'TODOHISTORY',

  watch: {
    todoId: {
      handler(id) {
        if (id) {
          console.log('todoId: ' + id);
          getTodoRecords(
            id,
            (res) => {
              console.log(res);
              for (let i = 0; i < res.object.length; i++) {
                let item = res.object[i];
                let temp = {
                  id: item['id'],
                  writer: item['diff']['writer'],
                  url: item['url'],
                  diff: item['diff'],
                  modifyDate: formatDate(item['modifyDate']),
                };
                this.todoHistoryList.push(temp);
              }
            },
            (error) => {
              alert('할일 히스토리 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
              console.log(error);
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
      todoHistoryList: [],
    };
  },
};
</script>
