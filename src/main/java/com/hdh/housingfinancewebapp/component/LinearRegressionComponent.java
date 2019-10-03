package com.hdh.housingfinancewebapp.component;

import org.springframework.stereotype.Component;

@Component
public class LinearRegressionComponent {
  private double intercept, slope;
  private double r2;
  private double svar0, svar1;

  public void process(int[] x, int[] y) {
    if (x.length != y.length) {
      throw new IllegalArgumentException("array lengths are not equal");
    }
    int n = x.length;

    // first pass
    double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
    for (int i = 0; i < n; i++) {
      sumx  += x[i];
      sumx2 += x[i]*x[i];
      sumy  += y[i];
    }
    double xbar = sumx / n;
    double ybar = sumy / n;

    // second pass: compute summary statistics
    double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
    for (int i = 0; i < n; i++) {
      xxbar += (x[i] - xbar) * (x[i] - xbar);
      yybar += (y[i] - ybar) * (y[i] - ybar);   // 상관계수 구할때 사용, 측정값과 실제 값의 차이의 제곱의 합
      xybar += (x[i] - xbar) * (y[i] - ybar);
    }
    slope  = xybar / xxbar;   // 최소 자승법에 의한 공식
    intercept = ybar - slope * xbar;

    // more statistical analysis
    double rss = 0.0;      // residual sum of squares, 회귀식으로 설명 불가한 잔차(실제 기울기와의 차이)
    double ssr = 0.0;      // regression sum of squares, 회귀식으로 설명 가능한 차의 합(ybar와의 차이)
    for (int i = 0; i < n; i++) {
      double fit = slope*x[i] + intercept;
      rss += (fit - y[i]) * (fit - y[i]);
      ssr += (fit - ybar) * (fit - ybar);
    }

    // F test
    int degreesOfFreedom = n-2;
    r2    = ssr / yybar;
    double svar  = rss / degreesOfFreedom;
    svar1 = svar / xxbar;
    svar0 = svar/n + xbar*xbar*svar1;
  }

  public double intercept() {
    return intercept;
  }

  public double slope() {
    return slope;
  }

  public double R2() {
    return r2;
  }

  public double interceptStdErr() {
    return Math.sqrt(svar0);
  }

  public double slopeStdErr() {
    return Math.sqrt(svar1);
  }

  public int predict(int x) {
    return (int) (slope*x + intercept);
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(String.format("%.2f n + %.2f", slope(), intercept()));
    s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
    return s.toString();
  }

}
