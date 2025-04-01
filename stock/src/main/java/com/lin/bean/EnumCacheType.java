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

package com.lin.bean;


import com.ugrong.framework.redis.domain.IRedisCacheType;

public enum EnumCacheType implements IRedisCacheType {

    /**
     * 信息缓存
     */
    DOCUMENT_CACHE("DUCUMENT"),
    INDUSTRY_BOARD_CACHE("INDUSTRY_BOARD"),
    CONCEPT_BOARD_CACHE("CONCEPT_BOARD"),
    INTRA_DAY_CACHE("INTRA_DAY"),
    ;

    private final String value;

    EnumCacheType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
