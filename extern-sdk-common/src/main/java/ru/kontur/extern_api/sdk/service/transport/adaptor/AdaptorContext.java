/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor;

import ru.kontur.extern_api.sdk.annotation.Context;

/**
 *
 * @author alexs
 */
public class AdaptorContext extends Context {
    
    private static volatile AdaptorContext INSTANCE;
    
    private static final String CONTEXT = "/AdaptorContext.xml";

    private AdaptorContext() {
        load(CONTEXT);
    }
    
    public static AdaptorContext getInstance() {
        AdaptorContext inst = AdaptorContext.INSTANCE;
        if (inst == null) {
            synchronized (AdaptorContext.class) {
                inst = AdaptorContext.INSTANCE;
                if (inst == null) {
                    AdaptorContext.INSTANCE = inst = new AdaptorContext();
                }
            }
        }
        return inst;
    }
}