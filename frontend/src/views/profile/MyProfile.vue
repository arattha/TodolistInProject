<template>
  <div
    class="
      flex
      xl:flex-row
      flex-col
      items-center
      mx-auto
      my-16
      bg-itemGray
      rounded-lg
      w-11/12
      md:w-7/12
      xl:h-80
      shadow-md
    "
  >
    <div class="flex items-center justify-center w-1/2 xl:my-0 my-5">
      <div
        class="
          flex
          justify-center
          items-center
          bg-contentGray
          rounded-full
          w-48
          h-48
          md:w-64 md:h-64
        "
      >
        <img src="@/images/logo.png" class="object-cover" />
      </div>
    </div>
    <div class="flex flex-col justify-center xl:items-start items-center w-1/2 xl:my-0 my-5">
      <div class="flex flex-col mb-5">
        <div class="font-black text-4xl">{{ userInfo.name }}</div>
        <div class="text-2xl">{{ userInfo.team }}</div>
      </div>
      <div class="flex flex-col">
        <div class="text-lg">{{ userInfo.email }}</div>
        <div class="text-lg">{{ userInfo.phone }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyProfile',
  components: {},
  created() {},
  data() {
    return {
      userInfo: {
        id: 1,
        name: '조성표',
        team: '프론트엔드팀',
        email: 'cspcsp@tip.com',
        phone: '01012341234',
      },
    };
  },
};
</script>
