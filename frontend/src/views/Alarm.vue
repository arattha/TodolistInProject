<template>
  <div>
    <Header />

    <button @click="remove">선택한 알림 삭제</button>
    <button @click="removeAll">모든 알림 삭제</button>

    <div v-if="alarmList">
      <div v-for="(alarm, index) in alarmList" :key="index">
        <input type="checkbox" v-model="checkList" :value="alarm.id" />
        <div
          class="
            flex flex-row
            h-14
            border-b-2 border-r-2 border-l-2
            cursor-pointer
            hover:bg-indigo-200
          "
          @click="goTodo(alarm.todoId)"
        >
          <div class="md:flex justify-center items-center p-2 h-full w-full border-r-2 mr-2">
            {{ alarm.content }}
          </div>
          <div class="flex justify-center items-center h-full w-32 text-center">
            {{ alarm.regDate.split('T')[0] }}<br />{{ alarm.regDate.split('T')[1] }}
          </div>
        </div>
      </div>
    </div>
    <Pagination class="mt-3" :pageCnt="pageCnt" :pageSize="pageSize" @paging="pagingMethod" />
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
    ...mapGetters(['id', 'curPage', 'isDel', 'totalAlarmCnt', 'alarmStomp']),
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
      this.alarmStomp.send(
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
