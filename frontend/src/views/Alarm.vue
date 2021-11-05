<template>
  <div>
    <div v-for="(alarm, index) in alarmList" :key="index">
      <div>
        {{ alarm }}
      </div>
      <button @click="check(alarm.id)"></button>
    </div>
  </div>
</template>

<script>
import { removeAll, removeAlarm } from "@/api/alarm.js";
import { mapGetters } from "vuex";

export default {
  name: "Alarm",
  data() {
    return {
      alarmList: [],
    };
  },
  created() {
    this.alarmList = this.$route.params.alarmList;
  },
  computed: {
    ...mapGetters(["id"]),
  },
  methods: {
    check(alarmId) {
      removeAlarm(
        {
          memberId: this.id,
          alarmId: alarmId,
        },
        (res) => {
          this.alarmList = res.object;

          this.$route.params.alarmStomp.send(
            "/server/checkAlarm",
            JSON.stringify({
              memberId: this.id,
              alarmId: alarmId,
            }),
            {}
          );
        },
        () => {}
      );
    },
    checkAll() {
      removeAll(
        {
          memberId: this.id,
        },
        () => {
          this.alarmList = [];

          this.$route.params.alarmStomp.send(
            "/server/checkAll",
            JSON.stringify({
              memberId: this.id,
            }),
            {}
          );
        },
        () => {}
      );
    },
    goTodo(todoId) {
      // 해당 Todo의 상세 페이지로 이동
      // this.$route.push("/todoInfo");
      console.log(todoId);
    },
  },
};
</script>

<style></style>
