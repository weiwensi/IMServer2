<template>
    <div>
        <div class="bubble_wrap">
            <div class="bubble" :class=" r ? 'bubble_right' : 'bubble_left'">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    r: Boolean
  }
}
</script>

<style>
.bubble{
    margin: 0 15px;
    position: relative;
    border-radius: 5px;
    padding: 5px;
    word-break:break-all;
    display:inline-block !important;
    line-height: 30px;
    max-width:650px;
    margin-bottom: 10px;
}
.bubble_right{
    background: #19be6b;
    position: absolute;
    right: 0;
}
.bubble_right::after{
    position: absolute;
    content: '';
    display: block;
    top: 10px;
    right: -5px;
    width: 0;
    height: 0;
    border-top: 5px solid transparent;
    border-left: 5px solid #19be6b;
    border-bottom: 5px solid transparent;
}
.bubble_left{
    background: #fff;
    color: black;
}
.bubble_left::after{
    position: absolute;
    content: '';
    display: block;
    top: 10px;
    left: -5px;
    width: 0;
    height: 0;
    border-top: 5px solid transparent;
    border-right: 5px solid #fff;
    border-bottom: 5px solid transparent;
}
.bubble_wrap{
    position: relative;
    height: 50px;
}
</style>
