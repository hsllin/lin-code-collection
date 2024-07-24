/**
 * MIT License
 * <p>
 * Copyright (c) 2019-2021 ugrong@163.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ugrong.framework.redis.samples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ugrong.framework.redis.repository.cache.IHashRedisRepository;
import com.ugrong.framework.redis.repository.cache.IListRedisRepository;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ugrong.framework.redis.domain.IRedisLockType;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import com.ugrong.framework.redis.repository.channel.IRedisChannelRepository;
import com.ugrong.framework.redis.repository.lock.IRedisLockRepository;
import com.ugrong.framework.redis.samples.model.Student;
import com.ugrong.framework.redis.samples.repository.StudentRedisRepository;
import com.ugrong.framework.redis.samples.service.IStudentService;
import com.ugrong.framework.redis.samples.type.EnumStudentCacheType;
import com.ugrong.framework.redis.samples.type.EnumStudentLockType;
import com.ugrong.framework.redis.samples.type.EnumStudentTopicType;
import org.springframework.util.CollectionUtils;

@SpringBootTest
class SamplesApplicationTests {

    @Autowired
    private IStringRedisRepository stringRedisRepository;

    @Autowired
    private IHashRedisRepository hashRedisRepository;

    @Autowired
    private IListRedisRepository listRedisRepository;


    @Autowired
    private StudentRedisRepository studentRedisRepository;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IRedisLockRepository redisLockRepository;

    @Autowired
    private IRedisChannelRepository redisChannelRepository;

    private Student getStudent() {
        Student student = new Student();
        student.setId(RandomUtils.nextLong());
        student.setName("张三");
        return student;
    }

    @Test
    void redisSimpleCache() {
        Student student = this.getStudent();
        String id = student.getId().toString();

        studentRedisRepository.set(id, student);
        System.out.println(studentRedisRepository.get(id).orElse(null));

        studentRedisRepository.remove(id);
    }

    @Test
    void redisStringCache() {
        Student student = this.getStudent();
        String id = student.getId().toString();

        stringRedisRepository.setStringValue(EnumStudentCacheType.STUDENT_CACHE, id);
        System.out.println(stringRedisRepository.getString(EnumStudentCacheType.STUDENT_CACHE).orElse(null));

//		stringRedisRepository.remove(EnumStudentCacheType.STUDENT_CACHE, id);

        //计数
        Long increment = stringRedisRepository.getAndIncrement(EnumStudentCacheType.STUDENT_CACHE, "xxx");
        System.out.println(increment);

//		stringRedisRepository.remove(EnumStudentCacheType.STUDENT_CACHE, "xxx");
    }

    @Test
    void redisListCache() {
        Student student = this.getStudent();
        String id = student.getId().toString();
        Student student2 = this.getStudent();
        String id2 = student2.getId().toString();
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(student2);

        listRedisRepository.addAll(id, studentList.toArray(new Student[0]));

//        listRedisRepository.add(id, student);
//		listRedisRepository.add(id, student2);
        System.out.println(listRedisRepository.get(id).get(0));

//		listRedisRepository.remove("studentList");

    }

    @Test
    void redisHashCache() {

        hashRedisRepository.put("studentMap", "key", "333");
        hashRedisRepository.put("studentMap", "lin", "ad");
//        listRedisRepository.add(id, student);
//		listRedisRepository.add(id, student2);
//        System.out.println(hashRedisRepository.get("studentMap", "key"));
        System.out.println("之前：" + hashRedisRepository.entries("studentMap"));
        hashRedisRepository.remove("studentMap", "key");
//		listRedisRepository.remove("studentList");
        System.out.println("之后：" + hashRedisRepository.entries("studentMap"));
        System.out.println(hashRedisRepository.get("studentMap", "key"));
    }


    /**
     * 模拟了两个线程同时对一个学生进行加锁
     */
    @Test
    void redisLock() throws InterruptedException {
        Student student = this.getStudent();
        final String id = student.getId().toString();


        new Thread(() -> this.tryLock(id)).start();

        new Thread(() -> this.tryLock(id)).start();

        new Thread(() -> this.tryLock(id)).start();
        this.tryLock(id);

        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 注解式redis锁
     */
    @Test
    public void redisAnonLock() {
        studentService.testAnonLock();
    }

    /**
     * 发布消息
     */
    @Test
    public void publishMessage() {
        Student student = this.getStudent();
        redisChannelRepository.publish(EnumStudentTopicType.STUDENT_TOPIC, student);
    }

    private void tryLock(String lockField) {
        IRedisLockType lockType = EnumStudentLockType.STUDENT_LOCK;
        AtomicBoolean isLock = new AtomicBoolean(Boolean.FALSE);
        try {
            isLock.set(redisLockRepository.tryLock(lockType, lockField, 20, 20, TimeUnit.SECONDS));
            if (isLock.get()) {
                System.out.println("获取到锁，当前线程名称：" + Thread.currentThread().getName());
                //休息3秒 模拟业务处理
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            System.out.println("获取锁失败：" + Thread.currentThread().getName());
            e.printStackTrace();
        } finally {
            if (isLock.get()) {
                redisLockRepository.unlock(lockType, lockField);
            }
        }
    }

}
