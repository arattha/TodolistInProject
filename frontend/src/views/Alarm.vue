<template>
  <div>
    <Header />
    <div class="flex my-5 mx-16 h-10">
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="remove"
      >
        선택 삭제
      </button>
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="removeAll"
      >
        모두 삭제
      </button>
    </div>

    <div
      v-if="alarmList"
      class="
        flex flex-col
        justify-center
        items-center
        mx-16
        w-11/12
        h-full
        rounded-lg
        shadow-lg
        py-5
        bg-itemGray
      "
    >
      <div
        class="
          flex flex-shrink-0
          justify-center
          items-center
          bg-contentGray
          w-11/12
          h-20
          hover:bg-menuGray
          rounded-lg
          shadow-lg
          my-2
        "
        v-for="(alarm, index) in alarmList"
        :key="index"
      >
        <input class="w-10 h-5" type="checkbox" v-model="checkList" :value="alarm.id" />
        <div class="flex flex-row h-full w-full cursor-pointer" @click="goTodo(alarm.todoId)">
          <div
            class="
              md:flex
              items-center
              p-2
              border-r-2 border-l-2 border-itemGray
              h-full
              w-10/12
              mr-2
            "
          >
            {{ alarm.content }}
          </div>
          <div class="flex justify-center items-center h-full w-2/12 text-center">
            {{ alarm.regDate.split('T')[0] }}<br />{{ alarm.regDate.split('T')[1] }}
          </div>
        </div>
      </div>
    </div>
    <Pagination
      class="flex justify-center items-center mt-3"
      :pageCnt="pageCnt"
      :pageSize="pageSize"
      @paging="pagingMethod"
    />
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm, getAlarmInMypage } from '@/api/alarm.js';
import { mapActions, mapGetters } from 'vuex';
import Pagination from '@/components/Pagination';
import Header from '@/components/Header.vue';

export default {
  name: 'Alarm',
  components: {
    Header,
    Pagination,
  },
  data() {
    return {
      alarmList: [],
      checkList: [],
      pageCnt: 5,
      pageSize: 6,
    };
  },
  created() {
    this.pageCnt = 5;
    this.pageSize = 6;
    this.getAlarm();
  },
  computed: {
    ...mapGetters(['id', 'curPage', 'isDel', 'totalAlarmCnt', 'stomp']),
  },
  methods: {
    ...mapActions([
      'set_totalAlarmCnt',
      'set_curPage',
      'set_offset',
      'toggle_isDel',
      'set_todo_id',
    ]),
    async getAlarm() {
      let userData = {
        id: this.id,
        page: this.curPage - 1,
        size: 6,
        sort: 'regDate,desc',
      };

      await getAlarmInMypage(
        userData,
        (res) => {
          this.alarmList = res.object.content;

          if (!this.alarmList && this.isDel) {
            this.set_curPage(this.curPage - 1);
            this.getAlarm();
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    pagingMethod(page) {
      this.checkList = [];
      this.getAlarm(page);
    },
    remove() {
      if (this.checkList.length == 0) {
        alert('선택한 알람이 없습니다.');
        return;
      }

      removeAlarm(
        {
          memberId: this.id,
          checkList: this.checkList,
        },
        () => {
          this.toggle_isDel(true);
          this.set_totalAlarmCnt(this.totalAlarmCnt - this.checkList.length);
          this.getAlarm();
          this.checkList = [];
          alert('삭제되었습니다.');
          this.getRealtimeAlarm();
        },
        () => {
          return;
        }
      );
    },
    removeAll() {
      if (this.totalAlarmCnt == 0) {
        alert('알람이 없습니다.');
        return;
      }

      removeAllAlarm(
        this.id,
        () => {
          this.toggle_isDel(true);
          this.set_totalAlarmCnt(0);
          this.getAlarm();
          this.getRealtimeAlarm();
        },
        () => {}
      );
    },
    goTodo(todoId) {
      // 해당 Todo의 상세 페이지로 이동
      this.set_todo_id(todoId);
      this.$router.push('/' + todoId + '/detail');
    },
    getRealtimeAlarm() {
      this.stomp.send(
        '/server/getAlarm',
        JSON.stringify({
          memberId: this.id,
        }),
        {}
      );
    },
  },
  // 해당 페이지에서 나갈때 값을 초기화하도록 세팅
  // 만약 새로고침 시에는 url이 같으므로 변경 X
  beforeRouteLeave(to, from, next) {
    // just use `this` this.name = to.params.name next()
    if (to.fullPath !== from.fullPath) {
      this.set_curPage(1);
      this.set_offset(0);
    }

    next();
  },
};
</script>

<style></style>
