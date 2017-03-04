/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
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
package nl.topicus.bitbucket.events;

import com.atlassian.bitbucket.repository.RefType;
import com.atlassian.bitbucket.repository.StandardRefType;

import java.util.Locale;

public class BitbucketPushChange
{
    private State _new;
    private State _old;
    private boolean created;
    private boolean closed;

    public State getNew()
    {
        return _new;
    }

    public void setNew(State _new)
    {
        this._new = _new;
    }

    public State getOld()
    {
        return _old;
    }

    public void setOld(State _old)
    {
        this._old = _old;
    }

    public boolean isCreated()
    {
        return created;
    }

    public void setCreated(boolean created)
    {
        this.created = created;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public void setClosed(boolean closed)
    {
        this.closed = closed;
    }

    public static class State
    {
        private String type;
        private String name;
        private Target target;

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Target getTarget()
        {
            return target;
        }

        public void setTarget(Target target)
        {
            this.target = target;
        }

        public void setType(RefType type)
        {
            for (StandardRefType t : StandardRefType.values())
            {
                if (t.equals(type))
                {
                    this.type = t.name().toLowerCase(Locale.ENGLISH);
                    return;
                }
            }
            this.type = null;
        }

        public static class Target
        {
            private String type = "commit";
            private String hash;

            public Target()
            {
            }

            public Target(String hash)
            {
                this.hash = hash;
            }

            public String getType()
            {
                return type;
            }

            public void setType(String type)
            {
                this.type = type;
            }

            public String getHash()
            {
                return hash;
            }

            public void setHash(String hash)
            {
                this.hash = hash;
            }
        }
    }
}
