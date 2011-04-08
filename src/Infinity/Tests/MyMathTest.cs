using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Infinity;
using Microsoft.Xna.Framework;
using NUnit.Framework;

namespace Tests
{
    [TestFixture]
    public class MyMathTest
    {
        [Test]
        public void TestReflectionAcrossALine()
        {
            Assert.AreEqual(new Vector2(5, 4), MyMath.ReflectAcrossLine(new Vector2(4, 5), new Vector2(1, 1)));
            Assert.AreEqual(new Vector2(5, -4), MyMath.ReflectAcrossLine(new Vector2(-4, 5), new Vector2(1, 1)));
            Assert.AreEqual(new Vector2(-5, -4), MyMath.ReflectAcrossLine(new Vector2(-4, -5), new Vector2(1, 1)));
            Assert.AreEqual(new Vector2(-5, 4), MyMath.ReflectAcrossLine(new Vector2(4, -5), new Vector2(1, 1)));
        }

    }
}
